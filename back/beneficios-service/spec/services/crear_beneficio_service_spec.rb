require "rails_helper"

RSpec.describe CrearBeneficioService do
  describe ".ejecutar" do
    it "crea un beneficio con atributos válidos" do
      params = { empleado_id: 1, nombre_beneficio: "Seguro médico", monto: 150000.00 }
      resultado = described_class.ejecutar(params)

      expect(resultado[:exito]).to be true
      expect(resultado[:beneficio]).to be_persisted
      expect(resultado[:beneficio].empleado_id).to eq(1)
      expect(resultado[:beneficio].nombre_beneficio).to eq("Seguro médico")
      expect(resultado[:beneficio].monto).to eq(150000.00)
    end

    it "falla sin empleado_id" do
      params = { nombre_beneficio: "Seguro médico", monto: 150000.00 }
      resultado = described_class.ejecutar(params)

      expect(resultado[:exito]).to be false
      expect(resultado[:errores]).to include("El ID del empleado no puede estar vacío")
    end

    it "falla sin nombre_beneficio" do
      params = { empleado_id: 1, monto: 150000.00 }
      resultado = described_class.ejecutar(params)

      expect(resultado[:exito]).to be false
      expect(resultado[:errores]).to include("El nombre del beneficio no puede estar vacío")
    end

    it "falla con monto inválido" do
      params = { empleado_id: 1, nombre_beneficio: "Seguro médico", monto: 0 }
      resultado = described_class.ejecutar(params)

      expect(resultado[:exito]).to be false
      expect(resultado[:errores]).to include("El monto debe ser mayor a 0")
    end
  end
end
