export interface IStorage {
  getRestPath(): string;
  getRestUploadPath(): string;
}

export class SystemStorage implements IStorage {
  getRestPath(): string { return '/storage/system' }
  getRestUploadPath(): string { return '/storage/system/upload' }
}

export class UserStorage implements IStorage {
  getRestPath(): string { return `/storage/user` }
  getRestUploadPath(): string { return '/storage/user/upload' }
}