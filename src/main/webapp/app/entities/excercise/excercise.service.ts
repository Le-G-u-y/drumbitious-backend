import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IExcercise } from 'app/shared/model/excercise.model';

type EntityResponseType = HttpResponse<IExcercise>;
type EntityArrayResponseType = HttpResponse<IExcercise[]>;

@Injectable({ providedIn: 'root' })
export class ExcerciseService {
  public resourceUrl = SERVER_API_URL + 'api/excercises';

  constructor(protected http: HttpClient) {}

  create(excercise: IExcercise): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(excercise);
    return this.http
      .post<IExcercise>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(excercise: IExcercise): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(excercise);
    return this.http
      .put<IExcercise>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IExcercise>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IExcercise[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(excercise: IExcercise): IExcercise {
    const copy: IExcercise = Object.assign({}, excercise, {
      createDate: excercise.createDate != null && excercise.createDate.isValid() ? excercise.createDate.toJSON() : null,
      modifyDate: excercise.modifyDate != null && excercise.modifyDate.isValid() ? excercise.modifyDate.toJSON() : null
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
      res.body.forEach((excercise: IExcercise) => {
        excercise.createDate = excercise.createDate != null ? moment(excercise.createDate) : null;
        excercise.modifyDate = excercise.modifyDate != null ? moment(excercise.modifyDate) : null;
      });
    }
    return res;
  }
}
