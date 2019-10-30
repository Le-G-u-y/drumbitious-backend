import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IExcerciseConfig } from 'app/shared/model/excercise-config.model';

type EntityResponseType = HttpResponse<IExcerciseConfig>;
type EntityArrayResponseType = HttpResponse<IExcerciseConfig[]>;

@Injectable({ providedIn: 'root' })
export class ExcerciseConfigService {
  public resourceUrl = SERVER_API_URL + 'api/excercise-configs';

  constructor(protected http: HttpClient) {}

  create(excerciseConfig: IExcerciseConfig): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(excerciseConfig);
    return this.http
      .post<IExcerciseConfig>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(excerciseConfig: IExcerciseConfig): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(excerciseConfig);
    return this.http
      .put<IExcerciseConfig>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IExcerciseConfig>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IExcerciseConfig[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(excerciseConfig: IExcerciseConfig): IExcerciseConfig {
    const copy: IExcerciseConfig = Object.assign({}, excerciseConfig, {
      createDate: excerciseConfig.createDate != null && excerciseConfig.createDate.isValid() ? excerciseConfig.createDate.toJSON() : null,
      modifyDate: excerciseConfig.modifyDate != null && excerciseConfig.modifyDate.isValid() ? excerciseConfig.modifyDate.toJSON() : null
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.createDate = res.body.createDate != null ? moment(res.body.createDate) : null;
      res.body.modifyDate = res.body.modifyDate != null ? moment(res.body.modifyDate) : null;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((excerciseConfig: IExcerciseConfig) => {
        excerciseConfig.createDate = excerciseConfig.createDate != null ? moment(excerciseConfig.createDate) : null;
        excerciseConfig.modifyDate = excerciseConfig.modifyDate != null ? moment(excerciseConfig.modifyDate) : null;
      });
    }
    return res;
  }
}
