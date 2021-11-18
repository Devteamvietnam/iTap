export const UnitRestURL = {
  unit: {
    load: (name: string) => { return `/company/settings/unit/${name}` },
    save: '/company/settings/unit',
    saveUnits: '/company/settings/units',
    search: "/company/settings/unit/search",
    saveState: '/company/settings/unit/storage-state',
  },

  unitGroup: {
    load: (name: string) => { return `/company/settings/unit/group/${name}` },
    search: "/company/settings/unit/group/search",
    save: "/company/settings/unit/group",
    saveState: '/company/settings/unit/group/storage-state',
  }
};

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

export const LocationRestURL = {
  serviceLocation: {
    load: (code: string) => { return `/company/settings/location/${code}`; },
    save: "/company/settings/location",
    search: "/company/settings/location/search",
    saveState: "/company/settings/location/storage-state"
  },
};


export const MiscRestURL = {
  appraisal: {
    load: (code: string) => { return `/company/misc/appraisal/config/${code}`; },
    save: '/company/misc/appraisal/config',
    search: '/company/misc/appraisal/config/search',
    saveState: '/company/misc/appraisal/config/storage-state'
  },

  appraisalConfig: {
    load: (name: string) => { return `/company/misc/appraisal/config/criteria/${name}`; },
    save: '/company/misc/appraisal/config/criteria',
    search: '/company/misc/appraisal/config/criteria/search',
    saveSate: '/company/misc/appraisal/config/storage-state'
  },

  appraisalReport: {
    load: (id: string) => { return `/company/misc/appraisal/report/${id}`; },
    save: '/company/misc/appraisal/report',
    search: '/company/misc/appraisal/report/search',
    saveState: '/company/misc/appraisal/report/storage-state'
  },

  targetSubject: {
    load: (id: string) => { return `/company/misc/appraisal/target/${id}`; },
    save: '/company/misc/appraisal/target',
    findId: (id: string) => { return `/company/misc/appraisal/target/find/${id}`; }
  },

  timeOff: {
    load: (code: string) => { return `company/hr/timeoff/${code}`; },
    save: 'company/hr/timeoff',
    search: `/company/hr/timeoff/search`,
    saveState: '/company/hr/timeoff/storage-state'
  },

  timeOffRemaining: {
    load: (code: string) => { return `company/hr/timeoff/remaining/${code}`; },
    save: 'company/hr/timeoff/remaining',
    search: '/company/hr/timeoff/remaining/search'
  },

  timeOffType: {
    load: (code: string) => { return `/company/hr/timeoff/type/${code}`; },
    save: `company/hr/timeoff/type`,
    search: `/company/hr/timeoff/type/search`,
    saveState: 'company/hr/timeoff/type/storage-state'
  },

  timesheetPeriod: {
    load: (code: string) => { return `/company/hr/timesheet/period/${code}`; },
    save: '/company/hr/timesheet/period',
    search: `/company/hr/timesheet/period/search`,
    saveState: '/company/hr/timesheet/period/storage-state',
    bulkCreate: `/company/hr/timesheet/period/bulk-create`,
    calculate: `/company/hr/timesheet/period/calculate`,
    bulkCalculate: `/company/hr/timesheet/period/bulk-calculate`
  },

  timesheetPeriodAdjustment: {
    load: (code: string) => { return `/company/hr/timesheet/period/adjustment/${code}`; },
    save: '/company/hr/timesheet/period/adjustment',
    search: `/company/hr/timesheet/period/adjustment/search`,
    bulkSave: '/company/hr/timesheet/period/adjustment/bulk-approval',
    bulkCreate: '/company/hr/timesheet/period/adjustment/bulk-create',
    saveState: '/company/hr/timesheet/period/adjustment/storage-state',
  },

  timesheetDaily: {
    load: (code: string) => { return `/company/hr/timesheet/daily/${code}`; },
    save: '/company/hr/timesheet/daily',
    search: `/company/hr/timesheet/daily/search`,
    saveState: '/company/hr/timesheet/daily/storage-state',
    update: `/company/hr/timesheet/daily/update`
  },

  timeTracking: {
    load: (code: string) => { return `/company/hr/timesheet/time-tracking/${code}`; },
    save: '/company/hr/timesheet/time-tracking',
    search: `/company/hr/timesheet/time-tracking/search`,
    findByTimesheetDailyCode: (timesheetDailyCode: string) => {
      return `/company/hr/timesheet/time-tracking/find/${timesheetDailyCode}`;
    },
  },

  timesheetProject: {
    load: (code: string) => { return `company/misc/timesheet/project/${code}`; },
    save: '/company/misc/timesheet/project',
    search: '/company/misc/timesheet/project/search',
    saveState: '/company/misc/timesheet/project/storage-state'
  },

  recruitPlan: {
    load: (id: number) => { return `company/misc/recruit/plan/${id}`; },
    save: '/company/misc/recruit/plan',
    search: '/company/misc/recruit/plan/search',
    saveState: '/company/misc/recruit/plan/storage-state'
  },


  recruitPosition: {
    search: '/company/misc/recruit/position/search',
  },

  applicant: {
    load: (id: number) => { return `company/misc/recruit/applicant/${id}`; },
    save: '/company/misc/recruit/applicant',
    add: '/company/misc/recruit/applicant/add',
    search: '/company/misc/recruit/applicant/search',
    saveState: '/company/misc/recruit/applicant/storage-state',
    delete: '/company/misc/recruit/applicant/delete',
  },

  candidate: {
    load: (id: number) => { return `company/misc/recruit/candidate/${id}`; },
    save: '/company/misc/recruit/candidate',
    search: '/company/misc/recruit/candidate/search',
    saveState: '/company/misc/recruit/candidate/storage-state',
    loadAttachments: (id: number) => { return `/company/misc/recruit/candidate/${id}/attachments` },
    saveAttachments: (id: number) => { return `/company/misc/recruit/candidate/${id}/attachments` },
  },

  skill: {
    load: (id: number) => { return `company/misc/recruit/skill/template/${id}`; },
    save: '/company/misc/recruit/skill/template',
    search: '/company/misc/recruit/skill/template/search',
    saveState: '/company/misc/recruit/skill/template/storage-state',
  },
}
