require "nokogiri"
require "net/http"
require "uri"
require "openssl"

class NominatimService
  CACHE = {}
  CACHE_TTL = Rails.application.config.nominatim[:cache_ttl]

  def self.buscar_por_ciudad(ciudad)
    return nil if ciudad.nil? || ciudad.strip.empty?

    clave = ciudad.strip.downcase

    entrada_cache = CACHE[clave]
    if entrada_cache && !entrada_cache[:expiracion].past?
      return entrada_cache[:ubicacion]
    end

    ubicacion = consultar_nominatim(ciudad)
    CACHE[clave] = { ubicacion: ubicacion, expiracion: Time.now + CACHE_TTL }
    ubicacion
  end

  def self.consultar_nominatim(ciudad)
    base_url = Rails.application.config.nominatim[:base_url]
    user_agent = Rails.application.config.nominatim[:user_agent]

    uri = URI(base_url)
    uri.query = URI.encode_www_form(q: ciudad, format: "xml", limit: 1)

    http = Net::HTTP.new(uri.host, uri.port)
    http.use_ssl = true
    http.open_timeout = 10
    http.read_timeout = 10
    http.verify_mode = OpenSSL::SSL::VERIFY_PEER

    begin
      http.cert_store = OpenSSL::X509::Store.new.tap { |s| s.set_default_paths }
    rescue
      http.verify_mode = OpenSSL::SSL::VERIFY_NONE
    end

    request = Net::HTTP::Get.new(uri.request_uri)
    request["User-Agent"] = user_agent

    response = http.request(request)

    return nil unless response.is_a?(Net::HTTPSuccess)
    return nil if response.body.nil? || response.body.strip.empty?

    parsear_xml(response.body)
  rescue StandardError => e
    nil
  end

  def self.parsear_xml(xml)
    doc = Nokogiri::XML(xml)
    lugar = doc.at_xpath("//place")

    return nil unless lugar

    {
      latitud: lugar["lat"],
      longitud: lugar["lon"],
      display_name: lugar["display_name"]
    }
  end
end
