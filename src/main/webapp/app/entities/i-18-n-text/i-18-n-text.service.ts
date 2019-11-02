import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { II18nText } from 'app/shared/model/i-18-n-text.model';

type EntityResponseType = HttpResponse<II18nText>;
type EntityArrayResponseType = HttpResponse<II18nText[]>;

@Injectable({ providedIn: 'root' })
export class I18nTextService {
  public resourceUrl = SERVER_API_URL + 'api/i-18-n-texts';

  constructor(protected http: HttpClient) {}

  create(i18nText: II18nText): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(i18nText);
    return this.http
      .post<II18nText>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(i18nText: II18nText): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(i18nText);
    return this.http
      .put<II18nText>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<II18nText>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<II18nText[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(i18nText: II18nText): II18nText {
    const copy: II18nText = Object.assign({}, i18nText, {
      createDate: i18nText.createDate != null && i18nText.createDate.isValid() ? i18nText.createDate.toJSON() : null,
      modifyDate: i18nText.modifyDate != null && i18nText.modifyDate.isValid() ? i18nText.modifyDate.toJSON() : null
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
      res.body.forEach((i18nText: II18nText) => {
        i18nText.createDate = i18nText.createDate != null ? moment(i18nText.createDate) : null;
        i18nText.modifyDate = i18nText.modifyDate != null ? moment(i18nText.modifyDate) : null;
      });
    }
    return res;
  }
}
