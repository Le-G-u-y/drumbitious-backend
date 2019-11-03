import { Moment } from 'moment';
import { IFinishedExercise } from 'app/shared/model/finished-exercise.model';

export interface IFinishedSession {
  id?: number;
  minutesTotal?: number;
  note?: string;
  createDate?: Moment;
  modifyDate?: Moment;
  planPlanName?: string;
  planId?: number;
  exercises?: IFinishedExercise[];
}

export class FinishedSession implements IFinishedSession {
  constructor(
    public id?: number,
    public minutesTotal?: number,
    public note?: string,
    public createDate?: Moment,
    public modifyDate?: Moment,
    public planPlanName?: string,
    public planId?: number,
    public exercises?: IFinishedExercise[]
  ) {}
}
