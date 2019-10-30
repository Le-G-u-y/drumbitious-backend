import { Moment } from 'moment';

export interface IPlan {
  id?: number;
  planName?: string;
  planFocus?: string;
  description?: string;
  minutesPerSession?: number;
  sessionsPerWeek?: number;
  targetDate?: Moment;
  active?: boolean;
  createDate?: Moment;
  modifyDate?: Moment;
  ownerLogin?: string;
  ownerId?: number;
  creatorLogin?: string;
  creatorId?: number;
  excerciseConfigId?: number;
}

export class Plan implements IPlan {
  constructor(
    public id?: number,
    public planName?: string,
    public planFocus?: string,
    public description?: string,
    public minutesPerSession?: number,
    public sessionsPerWeek?: number,
    public targetDate?: Moment,
    public active?: boolean,
    public createDate?: Moment,
    public modifyDate?: Moment,
    public ownerLogin?: string,
    public ownerId?: number,
    public creatorLogin?: string,
    public creatorId?: number,
    public excerciseConfigId?: number
  ) {
    this.active = this.active || false;
  }
}
