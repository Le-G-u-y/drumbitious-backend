import { Moment } from 'moment';
import { IPlan } from 'app/shared/model/plan.model';

export interface IExcerciseConfig {
  id?: number;
  practiceBpm?: number;
  targetBpm?: number;
  minutes?: number;
  note?: string;
  createDate?: Moment;
  modifyDate?: Moment;
  plans?: IPlan[];
  excerciseExcerciseName?: string;
  excerciseId?: number;
}

export class ExcerciseConfig implements IExcerciseConfig {
  constructor(
    public id?: number,
    public practiceBpm?: number,
    public targetBpm?: number,
    public minutes?: number,
    public note?: string,
    public createDate?: Moment,
    public modifyDate?: Moment,
    public plans?: IPlan[],
    public excerciseExcerciseName?: string,
    public excerciseId?: number
  ) {}
}
