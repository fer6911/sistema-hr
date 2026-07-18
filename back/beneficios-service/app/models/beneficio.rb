class Beneficio < ApplicationRecord
  validates :empleado_id, presence: { message: "El ID del empleado no puede estar vacío" }
  validates :nombre_beneficio, presence: { message: "El nombre del beneficio no puede estar vacío" }
  validates :monto, presence: { message: "El monto no puede estar vacío" },
                    numericality: { greater_than: 0, message: "El monto debe ser mayor a 0" }

  scope :por_empleado, ->(empleado_id) { where(empleado_id: empleado_id) }
end
