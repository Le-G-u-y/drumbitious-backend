import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IFinishedSession } from 'app/shared/model/finished-session.model';

type EntityResponseType = HttpResponse<IFinishedSession>;
type EntityArrayResponseType = HttpResponse<IFinishedSession[]>;

@Injectable({ providedIn: 'root' })
export class FinishedSessionService {
  public resourceUrl = SERVER_API_URL + 'api/finished-sessions';

  constructor(protected http: HttpClient) {}

  create(finishedSession: IFinishedSession): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(finishedSession);
    return this.http
      .post<IFinishedSession>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(finishedSession: IFinishedSession): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(finishedSession);
    return this.http
      .put<IFinishedSession>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IFinishedSession>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IFinishedSession[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(finishedSession: IFinishedSession): IFinishedSession {
    const copy: IFinishedSession = Object.assign({}, finishedSession, {
      createDate: finishedSession.createDate != null && finishedSession.createDate.isValid() ? finishedSession.createDate.toJSON() : null,
      modifyDate: finishedSession.modifyDate != null && finishedSession.modifyDate.isValid() ? finishedSession.modifyDate.toJSON() : null
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
      res.body.forEach((finishedSession: IFinishedSession) => {
        finishedSession.createDate = finishedSession.createDate != null ? moment(finishedSession.createDate) : null;
        finishedSession.modifyDate = finishedSession.modifyDate != null ? moment(finishedSession.modifyDate) : null;
      });
    }
    return res;
  }
}
