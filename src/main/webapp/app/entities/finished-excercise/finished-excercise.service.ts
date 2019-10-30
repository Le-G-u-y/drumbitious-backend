import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IFinishedExcercise } from 'app/shared/model/finished-excercise.model';

type EntityResponseType = HttpResponse<IFinishedExcercise>;
type EntityArrayResponseType = HttpResponse<IFinishedExcercise[]>;

@Injectable({ providedIn: 'root' })
export class FinishedExcerciseService {
  public resourceUrl = SERVER_API_URL + 'api/finished-excercises';

  constructor(protected http: HttpClient) {}

  create(finishedExcercise: IFinishedExcercise): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(finishedExcercise);
    return this.http
      .post<IFinishedExcercise>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(finishedExcercise: IFinishedExcercise): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(finishedExcercise);
    return this.http
      .put<IFinishedExcercise>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IFinishedExcercise>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IFinishedExcercise[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(finishedExcercise: IFinishedExcercise): IFinishedExcercise {
    const copy: IFinishedExcercise = Object.assign({}, finishedExcercise, {
      createDate:
        finishedExcercise.createDate != null && finishedExcercise.createDate.isValid() ? finishedExcercise.createDate.toJSON() : null,
      modifyDate:
        finishedExcercise.modifyDate != null && finishedExcercise.modifyDate.isValid() ? finishedExcercise.modifyDate.toJSON() : null
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
      res.body.forEach((finishedExcercise: IFinishedExcercise) => {
        finishedExcercise.createDate = finishedExcercise.createDate != null ? moment(finishedExcercise.createDate) : null;
        finishedExcercise.modifyDate = finishedExcercise.modifyDate != null ? moment(finishedExcercise.modifyDate) : null;
      });
    }
    return res;
  }
}
