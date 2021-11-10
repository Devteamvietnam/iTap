export * from './type'
export * from './UISearchParams'

export function createSearchFilter(pattern: string = "") {
  return [{ "name": "search", "type": "STRING_LIKE", "required": true, "filterValue": pattern }];
}

export function createEntityStateFilter() {
  return [
    {
      "name": "entityState", "label": "State", "type": "STRING", "required": true,
      "options": ["", "ACTIVE", "INACTIVE", "ARCHIVED", "JUNK", "DEPRECATED"],
      "optionLabels": ["All", "Active", "Inactive", "Archived", "Junk", "Deprecated"],
      "selectOption": "ACTIVE"
    }
  ];
}

export function createModifiedTimeFilter() {
  return [
    {
      "name": "modifiedTime", "label": "Modified Time", "type": "DATE", "required": true,
      "fromValue": null, "toValue": null
    }
  ];
}

export function createCreatedTimeFilter() {
  return [
    {
      "name": "createdTime", "label": "Created Time", "type": "DATE", "required": true,
      "fromValue": null, "toValue": null
    }
  ];
}

export function createDefaultOrderBy() {
  return {
    fields: ["modifiedTime"],
    fieldLabels: ["Modified Time"],
    selectFields: null,
    sort: "DESC"
  };
}

export function createOrderBy(fields: Array<string>, labels?: Array<string>) {
  return { fields: fields, fieldLabels: labels, selectFields: null, sort: "DESC" };
}

export function createStorageStateFilter(states: Array<"ACTIVE" | "INACTIVE" | "ARCHIVED" | "JUNK" | "DEPRECATED">) {
  let options = [""];
  let labels = ["All"];
  for (let i = 0; i < states.length; i++) {
    options.push(states[i]);
    let label = states[i].toLowerCase();
    label = label.charAt(0).toUpperCase() + label.slice(1);
    labels.push(label);
  }

  let filter = {
    name: 'storageState', label: 'State', type: 'STRING', required: true,
    options: options,
    optionLabels: labels,
    selectOption: states[0]
  };
  return filter;
}

export function createEditModeFilter(modes: Array<"DRAFT" | "VALIDATED" | "LOCKED">) {
  let options = [""];
  let labels = ["All"];
  for (let i = 0; i < modes.length; i++) {
    options.push(modes[i]);
    let label = modes[i].toLowerCase();
    label = label.charAt(0).toUpperCase() + label.slice(1);
    labels.push(label);
  }

  let filter = {
    name: 'editMode', label: 'Mode', type: 'STRING', required: true,
    options: options,
    optionLabels: labels,
    selectOption: ''
  };
  return filter;
}