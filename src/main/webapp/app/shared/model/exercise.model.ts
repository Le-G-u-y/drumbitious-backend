import { Moment } from 'moment';
import { SkillType } from 'app/shared/model/enumerations/skill-type.model';
import { ExerciseType } from 'app/shared/model/enumerations/exercise-type.model';

export interface IExercise {
  id?: number;
  exerciseName?: string;
  description?: string;
  sourceUrl?: string;
  defaultMinutes?: number;
  defaultBpmMin?: number;
  defaultBpmMax?: number;
  deactivted?: boolean;
  createDate?: Moment;
  modifyDate?: Moment;
  skillType?: SkillType;
  exercise?: ExerciseType;
  creatorLogin?: string;
  creatorId?: number;
}

export class Exercise implements IExercise {
  constructor(
    public id?: number,
    public exerciseName?: string,
    public description?: string,
    public sourceUrl?: string,
    public defaultMinutes?: number,
    public defaultBpmMin?: number,
    public defaultBpmMax?: number,
    public deactivted?: boolean,
    public createDate?: Moment,
    public modifyDate?: Moment,
    public skillType?: SkillType,
    public exercise?: ExerciseType,
    public creatorLogin?: string,
    public creatorId?: number
  ) {
    this.deactivted = this.deactivted || false;
  }
}
