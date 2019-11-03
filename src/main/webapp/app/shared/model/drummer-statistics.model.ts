import { Moment } from 'moment';

export interface IDrummerStatistics {
  id?: number;
  selfAssessedLevelSpeed?: number;
  selfAssessedLevelGroove?: number;
  selfAssessedLevelCreativity?: number;
  selfAssessedLevelAdaptability?: number;
  selfAssessedLevelDynamics?: number;
  selfAssessedLevelIndependence?: number;
  selfAssessedLevelLivePerformance?: number;
  selfAssessedLevelReadingMusic?: number;
  note?: string;
  createDate?: Moment;
  modifyDate?: Moment;
  userLogin?: string;
  userId?: number;
}

export class DrummerStatistics implements IDrummerStatistics {
  constructor(
    public id?: number,
    public selfAssessedLevelSpeed?: number,
    public selfAssessedLevelGroove?: number,
    public selfAssessedLevelCreativity?: number,
    public selfAssessedLevelAdaptability?: number,
    public selfAssessedLevelDynamics?: number,
    public selfAssessedLevelIndependence?: number,
    public selfAssessedLevelLivePerformance?: number,
    public selfAssessedLevelReadingMusic?: number,
    public note?: string,
    public createDate?: Moment,
    public modifyDate?: Moment,
    public userLogin?: string,
    public userId?: number
  ) {}
}
