type EventHandler = (...args: unknown[]) => void

class EventBus {
  private listeners: Map<string, EventHandler[]> = new Map()

  on(event: string, handler: EventHandler) {
    if (!this.listeners.has(event)) {
      this.listeners.set(event, [])
    }
    this.listeners.get(event)!.push(handler)
  }

  off(event: string, handler: EventHandler) {
    const handlers = this.listeners.get(event)
    if (handlers) {
      this.listeners.set(event, handlers.filter((h) => h !== handler))
    }
  }

  emit(event: string, ...args: unknown[]) {
    const handlers = this.listeners.get(event)
    if (handlers) {
      handlers.forEach((h) => h(...args))
    }
  }
}

export const eventBus = new EventBus()
