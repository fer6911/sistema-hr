class CrearBeneficios < ActiveRecord::Migration[7.1]
  def change
    create_table :beneficios do |t|
      t.bigint :empleado_id, null: false
      t.string :nombre_beneficio, null: false
      t.decimal :monto, precision: 10, scale: 2, null: false
      t.timestamps
    end

    add_index :beneficios, :empleado_id
  end
end
