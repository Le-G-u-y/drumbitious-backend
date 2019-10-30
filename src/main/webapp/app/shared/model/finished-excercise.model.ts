import { Moment } from 'moment';

export interface IFinishedExcercise {
  id?: number;
  actualBpm?: number;
  actualMinutes?: number;
  createDate?: Moment;
  modifyDate?: Moment;
  finishedSessionId?: number;
  excerciseExcerciseName?: string;
  excerciseId?: number;
}

export class FinishedExcercise implements IFinishedExcercise {
  constructor(
    public id?: number,
    public actualBpm?: number,
    public actualMinutes?: number,
    public createDate?: Moment,
    public modifyDate?: Moment,
    public finishedSessionId?: number,
    public excerciseExcerciseName?: string,
    public excerciseId?: number
  ) {}
}
