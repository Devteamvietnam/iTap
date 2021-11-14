
export const CustomFieldURL = {
  definitions: {
    search: `/core/entity/definitions/search`,
    load: (entity: string, type: string) => { return `/core/entity/${entity}/definitions/${type}` },
    save: (entity: string, type: string) => { return `/core/entity/${entity}/definitions/${type}` },
  },
  customFields: {
    load: (entity: string, type: string, entityId: number) => { return `/core/entity/${entity}/${type}/${entityId}` },
    save: (entity: string, type: string, entityId: number) => { return `/core/entity/${entity}/${type}/${entityId}` }
  }
}
