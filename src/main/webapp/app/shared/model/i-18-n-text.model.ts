import { Moment } from 'moment';

export interface II18nText {
  id?: number;
  locale?: string;
  textKey?: string;
  textContent?: string;
  createDate?: Moment;
  modifyDate?: Moment;
}

export class I18nText implements II18nText {
  constructor(
    public id?: number,
    public locale?: string,
    public textKey?: string,
    public textContent?: string,
    public createDate?: Moment,
    public modifyDate?: Moment
  ) {}
}
