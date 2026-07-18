FactoryBot.define do
  factory :beneficio do
    empleado_id { 1 }
    nombre_beneficio { Faker::Commerce.product_name }
    monto { Faker::Commerce.price(range: 10000.0..500000.0) }
  end
end
