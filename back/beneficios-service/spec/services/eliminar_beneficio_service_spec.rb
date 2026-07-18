require "rails_helper"

RSpec.describe EliminarBeneficioService do
  describe ".ejecutar" do
    it "elimina un beneficio existente" do
      beneficio = create(:beneficio)
      resultado = described_class.ejecutar(beneficio.id)

      expect(resultado[:exito]).to be true
      expect(Beneficio.find_by(id: beneficio.id)).to be_nil
    end

    it "falla si el beneficio no existe" do
      resultado = described_class.ejecutar(999)

      expect(resultado[:exito]).to be false
      expect(resultado[:errores]).to include("El beneficio no existe")
    end
  end
end
