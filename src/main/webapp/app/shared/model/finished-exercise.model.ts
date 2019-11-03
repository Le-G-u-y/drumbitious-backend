import { Moment } from 'moment';

export interface IFinishedExercise {
  id?: number;
  actualBpm?: number;
  actualMinutes?: number;
  createDate?: Moment;
  modifyDate?: Moment;
  finishedSessionId?: number;
  exerciseExerciseName?: string;
  exerciseId?: number;
}

export class FinishedExercise implements IFinishedExercise {
  constructor(
    public id?: number,
    public actualBpm?: number,
    public actualMinutes?: number,
    public createDate?: Moment,
    public modifyDate?: Moment,
    public finishedSessionId?: number,
    public exerciseExerciseName?: string,
    public exerciseId?: number
  ) {}
}
