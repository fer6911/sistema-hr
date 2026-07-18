# Seeds de ejemplo
Beneficio.create!(
  empleado_id: 1,
  nombre_beneficio: "Seguro médico",
  monto: 150000.00
)

Beneficio.create!(
  empleado_id: 1,
  nombre_beneficio: "Auxilio de transporte",
  monto: 50000.00
)

Beneficio.create!(
  empleado_id: 2,
  nombre_beneficio: "Bonificación de desempeño",
  monto: 300000.00
)

puts "Se crearon #{Beneficio.count} beneficios de ejemplo"
