export interface ISkillType {
  id?: number;
  skillTextKey?: string;
}

export class SkillType implements ISkillType {
  constructor(public id?: number, public skillTextKey?: string) {}
}
