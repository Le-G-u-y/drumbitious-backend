import { Moment } from 'moment';
import { IPlan } from 'app/shared/model/plan.model';

export interface IExerciseConfig {
  id?: number;
  practiceBpm?: number;
  targetBpm?: number;
  minutes?: number;
  note?: string;
  createDate?: Moment;
  modifyDate?: Moment;
  plans?: IPlan[];
  exerciseExerciseName?: string;
  exerciseId?: number;
}

export class ExerciseConfig implements IExerciseConfig {
  constructor(
    public id?: number,
    public practiceBpm?: number,
    public targetBpm?: number,
    public minutes?: number,
    public note?: string,
    public createDate?: Moment,
    public modifyDate?: Moment,
    public plans?: IPlan[],
    public exerciseExerciseName?: string,
    public exerciseId?: number
  ) {}
}
