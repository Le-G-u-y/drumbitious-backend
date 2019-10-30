import { Moment } from 'moment';
import { ISkillType } from 'app/shared/model/skill-type.model';
import { IExcerciseType } from 'app/shared/model/excercise-type.model';

export interface IExcercise {
  id?: number;
  excerciseName?: string;
  description?: string;
  sourceUrl?: string;
  defaultMinutes?: number;
  defaultBpmMin?: number;
  defaultBpmMax?: number;
  deactivted?: boolean;
  createDate?: Moment;
  modifyDate?: Moment;
  creatorLogin?: string;
  creatorId?: number;
  skillNames?: ISkillType[];
  excerciseTypes?: IExcerciseType[];
}

export class Excercise implements IExcercise {
  constructor(
    public id?: number,
    public excerciseName?: string,
    public description?: string,
    public sourceUrl?: string,
    public defaultMinutes?: number,
    public defaultBpmMin?: number,
    public defaultBpmMax?: number,
    public deactivted?: boolean,
    public createDate?: Moment,
    public modifyDate?: Moment,
    public creatorLogin?: string,
    public creatorId?: number,
    public skillNames?: ISkillType[],
    public excerciseTypes?: IExcerciseType[]
  ) {
    this.deactivted = this.deactivted || false;
  }
}
