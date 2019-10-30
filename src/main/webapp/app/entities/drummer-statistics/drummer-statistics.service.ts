import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IDrummerStatistics } from 'app/shared/model/drummer-statistics.model';

type EntityResponseType = HttpResponse<IDrummerStatistics>;
type EntityArrayResponseType = HttpResponse<IDrummerStatistics[]>;

@Injectable({ providedIn: 'root' })
export class DrummerStatisticsService {
  public resourceUrl = SERVER_API_URL + 'api/drummer-statistics';

  constructor(protected http: HttpClient) {}

  create(drummerStatistics: IDrummerStatistics): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(drummerStatistics);
    return this.http
      .post<IDrummerStatistics>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(drummerStatistics: IDrummerStatistics): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(drummerStatistics);
    return this.http
      .put<IDrummerStatistics>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IDrummerStatistics>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IDrummerStatistics[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(drummerStatistics: IDrummerStatistics): IDrummerStatistics {
    const copy: IDrummerStatistics = Object.assign({}, drummerStatistics, {
      createDate:
        drummerStatistics.createDate != null && drummerStatistics.createDate.isValid() ? drummerStatistics.createDate.toJSON() : null,
      modifyDate:
        drummerStatistics.modifyDate != null && drummerStatistics.modifyDate.isValid() ? drummerStatistics.modifyDate.toJSON() : null
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
      res.body.forEach((drummerStatistics: IDrummerStatistics) => {
        drummerStatistics.createDate = drummerStatistics.createDate != null ? moment(drummerStatistics.createDate) : null;
        drummerStatistics.modifyDate = drummerStatistics.modifyDate != null ? moment(drummerStatistics.modifyDate) : null;
      });
    }
    return res;
  }
}
