export interface IExcerciseType {
  id?: number;
  excerciseTypeTextKey?: string;
}

export class ExcerciseType implements IExcerciseType {
  constructor(public id?: number, public excerciseTypeTextKey?: string) {}
}
