require "rails_helper"

RSpec.describe ListarBeneficiosService do
  describe ".ejecutar" do
    it "lista beneficios de un empleado" do
      create(:beneficio, empleado_id: 1, nombre_beneficio: "Seguro médico")
      create(:beneficio, empleado_id: 1, nombre_beneficio: "Transporte")
      create(:beneficio, empleado_id: 2, nombre_beneficio: "Otro")

      resultado = described_class.ejecutar(1)

      expect(resultado[:exito]).to be true
      expect(resultado[:beneficios].count).to eq(2)
      expect(resultado[:beneficios].pluck(:nombre_beneficio)).to contain_exactly("Seguro médico", "Transporte")
    end

    it "retorna lista vacía si el empleado no tiene beneficios" do
      resultado = described_class.ejecutar(999)

      expect(resultado[:exito]).to be true
      expect(resultado[:beneficios]).to be_empty
    end
  end
end
