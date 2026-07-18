require "rails_helper"

RSpec.describe "Api::V1::Beneficios", type: :request do
  let(:xml_nominatim) do
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

  describe "POST /api/v1/beneficios" do
    it "crea un beneficio y retorna ApiResponse exitoso" do
      post "/api/v1/beneficios", params: {
        empleado_id: 1,
        nombre_beneficio: "Seguro médico",
        monto: 150000.00
      }

      expect(response).to have_http_status(:ok)
      body = JSON.parse(response.body)
      expect(body["error"]).to be false
      expect(body["message"]).to eq("Beneficio registrado exitosamente")
      expect(body["data"]["empleado_id"]).to eq(1)
      expect(body["data"]["nombre_beneficio"]).to eq("Seguro médico")
      expect(body["data"]["monto"]).to eq(150000.0)
    end

    it "retorna error con datos inválidos" do
      post "/api/v1/beneficios", params: {
        empleado_id: nil,
        nombre_beneficio: "",
        monto: -5
      }

      expect(response).to have_http_status(:unprocessable_entity)
      body = JSON.parse(response.body)
      expect(body["error"]).to be true
      expect(body["errors"]).not_to be_empty
    end
  end

  describe "GET /api/v1/beneficios/empleado/:empleado_id" do
    before do
      create(:beneficio, empleado_id: 1, nombre_beneficio: "Seguro médico", monto: 150000.00)
      create(:beneficio, empleado_id: 1, nombre_beneficio: "Transporte", monto: 50000.00)
    end

    it "lista beneficios sin ubicación" do
      get "/api/v1/beneficios/empleado/1"

      expect(response).to have_http_status(:ok)
      body = JSON.parse(response.body)
      expect(body["error"]).to be false
      expect(body["data"]["beneficios"].length).to eq(2)
      expect(body["data"]["ubicacion"]).to be_nil
    end

    it "lista beneficios con ubicación de Nominatim" do
      stub_request(:get, /nominatim\.openstreetmap\.org/)
        .to_return(status: 200, body: xml_nominatim, headers: { "Content-Type" => "text/xml" })

      get "/api/v1/beneficios/empleado/1", params: { ciudad: "Bogotá" }

      expect(response).to have_http_status(:ok)
      body = JSON.parse(response.body)
      expect(body["data"]["beneficios"].length).to eq(2)
      expect(body["data"]["ubicacion"]).not_to be_nil
      expect(body["data"]["ubicacion"]["latitud"]).to eq("4.7110")
      expect(body["data"]["ubicacion"]["longitud"]).to eq("-74.0721")
    end

    it "retorna lista vacía para empleado sin beneficios" do
      get "/api/v1/beneficios/empleado/999"

      expect(response).to have_http_status(:ok)
      body = JSON.parse(response.body)
      expect(body["data"]["beneficios"]).to be_empty
    end
  end

  describe "PUT /api/v1/beneficios/:id" do
    let!(:beneficio) { create(:beneficio, nombre_beneficio: "Seguro médico", monto: 150000.00) }

    it "actualiza un beneficio existente" do
      put "/api/v1/beneficios/#{beneficio.id}", params: {
        nombre_beneficio: "Seguro dental",
        monto: 200000.00
      }

      expect(response).to have_http_status(:ok)
      body = JSON.parse(response.body)
      expect(body["error"]).to be false
      expect(body["data"]["nombre_beneficio"]).to eq("Seguro dental")
      expect(body["data"]["monto"]).to eq(200000.0)
    end

    it "retorna error si el beneficio no existe" do
      put "/api/v1/beneficios/999", params: {
        nombre_beneficio: "Nuevo",
        monto: 100
      }

      expect(response).to have_http_status(:unprocessable_entity)
      body = JSON.parse(response.body)
      expect(body["error"]).to be true
    end
  end

  describe "DELETE /api/v1/beneficios/:id" do
    it "elimina un beneficio existente" do
      beneficio = create(:beneficio)

      delete "/api/v1/beneficios/#{beneficio.id}"

      expect(response).to have_http_status(:ok)
      body = JSON.parse(response.body)
      expect(body["error"]).to be false
      expect(body["message"]).to eq("Beneficio eliminado exitosamente")
      expect(Beneficio.find_by(id: beneficio.id)).to be_nil
    end

    it "retorna error si el beneficio no existe" do
      delete "/api/v1/beneficios/999"

      expect(response).to have_http_status(:unprocessable_entity)
      body = JSON.parse(response.body)
      expect(body["error"]).to be true
    end
  end
end
