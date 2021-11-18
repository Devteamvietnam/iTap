export const CompanyRestURL = {
  company: {
    search: "/company/search",
    load: (id: number) => { return `/company/id/${id}`; },
    save: "/company/update",
    create: "/company/create",
  },

  config: {
    load: (id: number) => { return `/company/config/id/${id}`; },
    save: "/company/config/save",
  },
};