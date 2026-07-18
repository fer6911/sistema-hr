require "rails_helper"

RSpec.describe Beneficio, type: :model do
  describe "validaciones" do
    it "es válido con atributos válidos" do
      beneficio = build(:beneficio)
      expect(beneficio).to be_valid
    end

    it "requiere empleado_id" do
      beneficio = build(:beneficio, empleado_id: nil)
      expect(beneficio).not_to be_valid
      expect(beneficio.errors[:empleado_id]).to include("El ID del empleado no puede estar vacío")
    end

    it "requiere nombre_beneficio" do
      beneficio = build(:beneficio, nombre_beneficio: nil)
      expect(beneficio).not_to be_valid
      expect(beneficio.errors[:nombre_beneficio]).to include("El nombre del beneficio no puede estar vacío")
    end

    it "requiere monto" do
      beneficio = build(:beneficio, monto: nil)
      expect(beneficio).not_to be_valid
      expect(beneficio.errors[:monto]).to include("El monto no puede estar vacío")
    end

    it "requiere monto mayor a 0" do
      beneficio = build(:beneficio, monto: 0)
      expect(beneficio).not_to be_valid
      expect(beneficio.errors[:monto]).to include("El monto debe ser mayor a 0")
    end

    it "rechaza monto negativo" do
      beneficio = build(:beneficio, monto: -100)
      expect(beneficio).not_to be_valid
    end
  end

  describe "scopes" do
    it ".por_empleado filtra por empleado_id" do
      create(:beneficio, empleado_id: 1)
      create(:beneficio, empleado_id: 1)
      create(:beneficio, empleado_id: 2)

      expect(Beneficio.por_empleado(1).count).to eq(2)
      expect(Beneficio.por_empleado(2).count).to eq(1)
    end
  end
end
