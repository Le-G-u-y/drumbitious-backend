import { Moment } from 'moment';
import { IFinishedExcercise } from 'app/shared/model/finished-excercise.model';

export interface IFinishedSession {
  id?: number;
  minutesTotal?: number;
  note?: string;
  createDate?: Moment;
  modifyDate?: Moment;
  planPlanName?: string;
  planId?: number;
  excercises?: IFinishedExcercise[];
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
    public excercises?: IFinishedExcercise[]
  ) {}
}
