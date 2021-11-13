import * as app from './app'
import {AppCapability} from './permission'

export interface IAppPlugin {
  targetModule: string;
  targetAppName: string;
  pluginType?: string;

  pluginName: string;
  pluginLabel: string;
  pluginDescription: string;
}

export interface IAppRegistry {
  module: string;
  name: string;
  label: string;
  description: string;
  createUI: (ctx: app.OSContext) => any;

  getRequiredAppCapability: () => AppCapability;
  setRequiredAppCapability: (permission: AppCapability) => void;

  getUserAppCapability: () => AppCapability;
  setUserAppCapability: (permission: AppCapability) => void;
}
