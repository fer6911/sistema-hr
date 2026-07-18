require "rails_helper"

RSpec.describe EditarBeneficioService do
  describe ".ejecutar" do
    let!(:beneficio) { create(:beneficio, nombre_beneficio: "Seguro médico", monto: 150000.00) }

    it "actualiza un beneficio existente" do
      resultado = described_class.ejecutar(beneficio.id, { nombre_beneficio: "Seguro dental", monto: 200000.00 })

      expect(resultado[:exito]).to be true
      expect(resultado[:beneficio].nombre_beneficio).to eq("Seguro dental")
      expect(resultado[:beneficio].monto).to eq(200000.00)
    end

    it "falla si el beneficio no existe" do
      resultado = described_class.ejecutar(999, { nombre_beneficio: "Nuevo", monto: 100 })

      expect(resultado[:exito]).to be false
      expect(resultado[:errores]).to include("El beneficio no existe")
    end
  end
end
