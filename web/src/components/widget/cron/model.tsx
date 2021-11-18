export type CronExpressionModel = {
  second: string;
  minute: string;
  hour: string;
  dayOfMonth: string;
  month: string;
  dayOfWeek: string;
}

export const newCronExpressionModel : CronExpressionModel = {
  second: '*',
  minute: '*',
  hour: '*',
  dayOfMonth: '*',
  month: '*',
  dayOfWeek: '?',
}

export function getCronExpression(model: CronExpressionModel): string {
  return `${model.second} ${model.minute} ${model.hour} ${model.dayOfMonth} ${model.month} ${model.dayOfWeek}`;
}

export function getCronExpressionModel(exp: string) {
  if (!exp || exp === '') return newCronExpressionModel;
  let arr = exp.split(' ');
  let model : any = {};
  model.second = arr[0].toString();
  model.minute = arr[1].toString();
  model.hour = arr[2].toString();
  model.dayOfMonth = arr[3].toString();
  model.month = arr[4].toString();
  model.dayOfWeek = arr[5].toString();
  if(arr.length == 7) {
    model.year = arr[6].toString();
  } else {
    model.year = '*';
  }
  return model;
}

export class TimeUnitValue {
  label: string;
  value: number;

  constructor(label: string, value: number) {
    this.label = label;
    this.value = value;
  }
}

export class EveryTimeUnitBetweenModel {
  label: string;
  minRange: number;
  maxRange: number;
  from: number;
  to: number;

  constructor(label: string, minRange: number, maxRange: number) {
    this.label = label;
    this.minRange = minRange;
    this.maxRange = maxRange;
    this.from = minRange;
    this.to   = maxRange;
  }

  getUnitExpression() {
    return `${this.from.toString()}-${this.to.toString()}`;
  }

  canEditExpression(exp: string) {
    if (exp.includes("-")) {
      this.update(exp);
      return true;
    }
    return false;
  }

  update(exp: string) {
    this.from = Number(exp.split("-")[0]);
    this.to = Number(exp.split("-")[1]);
  }
}

export class EveryTimeUnitModel {
  label: string;
  startAtOpts: Array<TimeUnitValue>;
  everyOpts: Array<number>;
  startAt: number;
  every: number;

  constructor(label: string, startAtOpts: Array<TimeUnitValue>, everyOpts: Array<number>) {
    this.label = label;
    this.startAtOpts = startAtOpts;
    this.everyOpts = everyOpts;
    this.startAt = startAtOpts[0].value;
    this.every = everyOpts[0];
  }

  getUnitExpression() {
    return `${this.startAt}/${this.every}`;
  }

  canEditExpression(exp: string) {
    if (exp.includes("/")) {
      this.update(exp);
      return true;
    }
    return false;
  }

  update(exp: string) {
    this.startAt = Number(exp.split("/")[0]);
    this.every = Number(exp.split("/")[1]);
  }
}

export class SpecificTimeUnitModel {
  label: string;
  minRange: number;
  maxRange: number;
  selectedUnits: Set<number>;

  constructor(label: string, minRange: number, maxRange: number, curExp: string) {
    this.label = label;
    this.minRange = minRange;
    this.maxRange = maxRange;
    this.selectedUnits = new Set<number>([]);
    if(curExp.indexOf(',') < 0) curExp = `${minRange}`; 
    let selUnits = curExp.split(',');
    for(let selUnit of selUnits) {
      let num = parseInt(selUnit);
      this.selectedUnits.add(num);
    }
  }

  select(value: number) { 
    this.selectedUnits.add(value); 
  }

  deselect(value: number) { 
    this.selectedUnits.delete(value); 
  }

  isEmpty() { return this.selectedUnits.size == 0 ;}

  contains(value: number) { 
    return this.selectedUnits.has(value); 
  }

  getUnitExpression() {
    let exp = '';
    if (this.selectedUnits.size === 0) return '*';
    let arrSelectedUnit = Array.from(this.selectedUnits).sort((a: number, b: number) => { return a - b });
    for (let i = 0; i < arrSelectedUnit.length; i++) {
      exp +=  arrSelectedUnit[i];
      if (i < arrSelectedUnit.length - 1) exp += ',';
    }
    return exp;
  }

  canEditExpression(exp: string) {
    if (exp.includes(",") || !isNaN(+exp)) {
      this.update(exp);
      return true;
    }
    return false;
  }

  update(exp: string) {
    let arr = exp.split(",");
    this.selectedUnits.clear();
    for (let i = 0; i < arr.length; i++) {
      this.selectedUnits.add(Number(arr[i]));
    }
  }
}
