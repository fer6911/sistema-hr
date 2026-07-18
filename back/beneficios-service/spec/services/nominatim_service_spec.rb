require "rails_helper"

RSpec.describe NominatimService do
  let(:xml_response) do
    <<~XML
      <?xml version="1.0" encoding="UTF-8"?>
      <searchresults>
        <place lat="4.7110" lon="-74.0721" display_name="Bogotá, Colombia"/>
      </searchresults>
    XML
  end

  before do
    NominatimService::CACHE.clear
  end

  describe ".buscar_por_ciudad" do
    it "retorna ubicación para una ciudad válida" do
      stub_request(:get, /nominatim\.openstreetmap\.org/)
        .to_return(status: 200, body: xml_response, headers: { "Content-Type" => "text/xml" })

      resultado = described_class.buscar_por_ciudad("Bogotá")

      expect(resultado).not_to be_nil
      expect(resultado[:latitud]).to eq("4.7110")
      expect(resultado[:longitud]).to eq("-74.0721")
      expect(resultado[:display_name]).to eq("Bogotá, Colombia")
    end

    it "retorna nil para ciudad vacía" do
      expect(described_class.buscar_por_ciudad("")).to be_nil
      expect(described_class.buscar_por_ciudad(nil)).to be_nil
      expect(described_class.buscar_por_ciudad("   ")).to be_nil
    end

    it "retorna nil cuando Nominatim retorna resultados vacíos" do
      empty_xml = '<?xml version="1.0" encoding="UTF-8"?><searchresults/>'
      stub_request(:get, /nominatim\.openstreetmap\.org/)
        .to_return(status: 200, body: empty_xml, headers: { "Content-Type" => "text/xml" })

      resultado = described_class.buscar_por_ciudad("CiudadInexistente")
      expect(resultado).to be_nil
    end

    it "retorna nil cuando hay error de red" do
      stub_request(:get, /nominatim\.openstreetmap\.org/)
        .to_timeout

      resultado = described_class.buscar_por_ciudad("Bogotá")
      expect(resultado).to be_nil
    end

    it "usa caché en la segunda llamada" do
      stub = stub_request(:get, /nominatim\.openstreetmap\.org/)
        .to_return(status: 200, body: xml_response, headers: { "Content-Type" => "text/xml" })

      described_class.buscar_por_ciudad("Bogotá")
      described_class.buscar_por_ciudad("Bogotá")

      expect(stub).to have_been_requested.once
    end

    it "normaliza la ciudad para la caché" do
      stub = stub_request(:get, /nominatim\.openstreetmap\.org/)
        .to_return(status: 200, body: xml_response, headers: { "Content-Type" => "text/xml" })

      described_class.buscar_por_ciudad("Bogotá")
      described_class.buscar_por_ciudad("  bogotá  ")

      expect(stub).to have_been_requested.once
    end
  end
end
