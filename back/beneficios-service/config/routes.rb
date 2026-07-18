Rails.application.routes.draw do
  namespace :api do
    namespace :v1 do
      get "beneficios/empleado/:empleado_id", to: "beneficios#listar_por_empleado"
      post "beneficios", to: "beneficios#crear"
      put "beneficios/:id", to: "beneficios#editar"
      delete "beneficios/:id", to: "beneficios#eliminar"
    end
  end

  get "up" => "rails/health#show", as: :rails_health_check
end
