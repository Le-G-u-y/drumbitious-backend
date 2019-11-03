import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IFinishedExercise } from 'app/shared/model/finished-exercise.model';

type EntityResponseType = HttpResponse<IFinishedExercise>;
type EntityArrayResponseType = HttpResponse<IFinishedExercise[]>;

@Injectable({ providedIn: 'root' })
export class FinishedExerciseService {
  public resourceUrl = SERVER_API_URL + 'api/finished-exercises';

  constructor(protected http: HttpClient) {}

  create(finishedExercise: IFinishedExercise): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(finishedExercise);
    return this.http
      .post<IFinishedExercise>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(finishedExercise: IFinishedExercise): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(finishedExercise);
    return this.http
      .put<IFinishedExercise>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IFinishedExercise>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IFinishedExercise[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(finishedExercise: IFinishedExercise): IFinishedExercise {
    const copy: IFinishedExercise = Object.assign({}, finishedExercise, {
      createDate:
        finishedExercise.createDate != null && finishedExercise.createDate.isValid() ? finishedExercise.createDate.toJSON() : null,
      modifyDate: finishedExercise.modifyDate != null && finishedExercise.modifyDate.isValid() ? finishedExercise.modifyDate.toJSON() : null
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
      res.body.forEach((finishedExercise: IFinishedExercise) => {
        finishedExercise.createDate = finishedExercise.createDate != null ? moment(finishedExercise.createDate) : null;
        finishedExercise.modifyDate = finishedExercise.modifyDate != null ? moment(finishedExercise.modifyDate) : null;
      });
    }
    return res;
  }
}
