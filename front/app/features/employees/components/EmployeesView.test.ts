import { describe, it, expect, vi, beforeEach } from 'vitest'
import { mount } from '@vue/test-utils'
import { reactive } from 'vue'
import { mockNuxtImport } from '@nuxt/test-utils/runtime'
import EmployeesView from './EmployeesView.vue'

const {
  fetchEmployeesMock,
  deleteEmployeeMock,
  navigateToMock,
  openLoginMock,
  mockStore,
} = vi.hoisted(() => {
  return {
    fetchEmployeesMock: vi.fn(),
    deleteEmployeeMock: vi.fn(),
    navigateToMock: vi.fn(),
    openLoginMock: vi.fn(),
    mockStore: { employees: [] as any[], loading: false },
  }
})

let isAuthenticated = true

mockNuxtImport('useEmployeesStore', () => {
  return () => ({
    ...mockStore,
    fetchEmployees: fetchEmployeesMock,
    deleteEmployee: deleteEmployeeMock,
  })
})

mockNuxtImport('useAuthStore', () => {
  return () => ({
    get isAuthenticated() {
      return isAuthenticated
    },
  })
})

mockNuxtImport('useAuthModalStore', () => {
  return () => ({
    openLogin: openLoginMock,
  })
})

mockNuxtImport('navigateTo', () => navigateToMock)

const empleadosMock = (n: number) =>
  Array.from({ length: n }, (_, i) => ({
    id: i + 1,
    nombre: `Nombre${i + 1}`,
    apellido: `Apellido${i + 1}`,
    email: `empleado${i + 1}@test.com`,
    cargo: i % 2 === 0 ? 'Desarrollador' : 'Diseñador',
    ciudad: i % 2 === 0 ? 'Bogota' : 'Medellin',
    salario: 50000,
    fechaIngreso: '2026-07-17',
    activo: i % 3 !== 0,
  }))

const mountView = () =>
  mount(EmployeesView, {
    global: {
      stubs: {
        CreateEmployeeModal: true,
        ConfirmDialog: true,
        Icon: true,
      },
    },
  })

describe('EmployeesView', () => {
  beforeEach(() => {
    vi.clearAllMocks()
    isAuthenticated = true
    mockStore.employees = []
    mockStore.loading = false
  })

  it('llama a fetchEmployees al montar si la lista está vacía', () => {
    mountView()

    expect(fetchEmployeesMock).toHaveBeenCalledOnce()
  })

  it('NO llama a fetchEmployees si ya hay empleados cargados', () => {
    mockStore.employees = empleadosMock(3)

    mountView()

    expect(fetchEmployeesMock).not.toHaveBeenCalled()
  })

  it('muestra el estado de carga cuando loading es true', () => {
    mockStore.loading = true

    const wrapper = mountView()

    expect(wrapper.text()).toContain('Cargando empleados…')
  })

  it('renderiza la lista de empleados', () => {
    mockStore.employees = empleadosMock(2)

    const wrapper = mountView()

    expect(wrapper.text()).toContain('Nombre1 Apellido1')
    expect(wrapper.text()).toContain('Nombre2 Apellido2')
  })

  it('muestra el mensaje de "sin resultados" cuando el filtro no coincide', async () => {
    mockStore.employees = empleadosMock(2)

    const wrapper = mountView()
    await wrapper.find('input[type="text"]').setValue('ciudad-inexistente')

    expect(wrapper.text()).toContain('No hay empleados que coincidan con tu búsqueda.')
  })

  it('filtra por nombre según el texto de búsqueda', async () => {
    mockStore.employees = empleadosMock(3)

    const wrapper = mountView()
    await wrapper.find('input[type="text"]').setValue('Nombre2')

    expect(wrapper.text()).toContain('Nombre2 Apellido2')
    expect(wrapper.text()).not.toContain('Nombre1 Apellido1')
  })

  it('filtra por estado "Activos"', async () => {
    mockStore.employees = empleadosMock(3)

    const wrapper = mountView()
    const activosButton = wrapper.findAll('button').find((b) => b.text() === 'Activos')
    await activosButton!.trigger('click')

    expect(wrapper.text()).not.toContain('Nombre1 Apellido1')
    expect(wrapper.text()).toContain('Nombre2 Apellido2')
  })

  it('filtra por estado "Inactivos"', async () => {
    mockStore.employees = empleadosMock(3)

    const wrapper = mountView()
    const inactivosButton = wrapper.findAll('button').find((b) => b.text() === 'Inactivos')
    await inactivosButton!.trigger('click')

    expect(wrapper.text()).toContain('Nombre1 Apellido1')
    expect(wrapper.text()).not.toContain('Nombre2 Apellido2')
  })

 it('resetea la página a 1 cuando cambia el filtro de búsqueda', async () => {
  mockStore.employees = empleadosMock(20)

  const wrapper = mountView()
  const page2Button = wrapper.findAll('button').find((b) => b.text() === '2')
  await page2Button!.trigger('click')
  expect(wrapper.text()).toContain('Página 2 de')

  // "Desarrollador" coincide con los índices pares (10 de los 20 mocks) -> sigue habiendo más de 1 página
  await wrapper.find('input[type="text"]').setValue('Desarrollador')

  expect(wrapper.text()).toContain('Página 1 de')
})

  it('pagina correctamente mostrando solo 8 empleados por página', () => {
    mockStore.employees = empleadosMock(20)

    const wrapper = mountView()

    expect(wrapper.text()).toContain('Nombre1 Apellido1')
    expect(wrapper.text()).toContain('Nombre8 Apellido8')
    expect(wrapper.text()).not.toContain('Nombre9 Apellido9')
  })

  it('navega a la página siguiente al hacer click en "Siguiente"', async () => {
    mockStore.employees = empleadosMock(20)

    const wrapper = mountView()
    const siguienteButton = wrapper.findAll('button').find((b) => b.text() === 'Siguiente')
    await siguienteButton!.trigger('click')

    expect(wrapper.text()).toContain('Nombre9 Apellido9')
  })

  it('muestra el botón "Nuevo empleado" cuando está autenticado', () => {
    isAuthenticated = true

    const wrapper = mountView()

    expect(wrapper.text()).toContain('Nuevo empleado')
  })

  it('muestra "Inicie sesión" cuando NO está autenticado', () => {
    isAuthenticated = false

    const wrapper = mountView()

    expect(wrapper.text()).toContain('Inicie sesión para gestionar empleados')
    expect(wrapper.text()).not.toContain('Nuevo empleado')
  })

  it('navega al detalle del empleado al hacer click en una fila', async () => {
    mockStore.employees = empleadosMock(1)

    const wrapper = mountView()
    await wrapper.find('tbody tr').trigger('click')

    expect(navigateToMock).toHaveBeenCalledWith('/empleados/1')
  })

  it('abre el modal de login al intentar eliminar sin estar autenticado', async () => {
    isAuthenticated = false
    mockStore.employees = empleadosMock(1)

    const wrapper = mountView()
    const deleteButton = wrapper.find('tbody tr button')
    await deleteButton.trigger('click')

    expect(openLoginMock).toHaveBeenCalledOnce()
  })

  it('NO navega a la fila al hacer click en el botón de eliminar (stopPropagation)', async () => {
    mockStore.employees = empleadosMock(1)

    const wrapper = mountView()
    const deleteButton = wrapper.find('tbody tr button')
    await deleteButton.trigger('click')

    expect(navigateToMock).not.toHaveBeenCalled()
  })
})