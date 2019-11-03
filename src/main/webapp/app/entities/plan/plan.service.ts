import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IPlan } from 'app/shared/model/plan.model';

type EntityResponseType = HttpResponse<IPlan>;
type EntityArrayResponseType = HttpResponse<IPlan[]>;

@Injectable({ providedIn: 'root' })
export class PlanService {
  public resourceUrl = SERVER_API_URL + 'api/plans';

  constructor(protected http: HttpClient) {}

  create(plan: IPlan): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(plan);
    return this.http
      .post<IPlan>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(plan: IPlan): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(plan);
    return this.http
      .put<IPlan>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IPlan>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPlan[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(plan: IPlan): IPlan {
    const copy: IPlan = Object.assign({}, plan, {
      targetDate: plan.targetDate != null && plan.targetDate.isValid() ? plan.targetDate.toJSON() : null,
      createDate: plan.createDate != null && plan.createDate.isValid() ? plan.createDate.toJSON() : null,
      modifyDate: plan.modifyDate != null && plan.modifyDate.isValid() ? plan.modifyDate.toJSON() : null
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.targetDate = res.body.targetDate != null ? moment(res.body.targetDate) : null;
      res.body.createDate = res.body.createDate != null ? moment(res.body.createDate) : null;
      res.body.modifyDate = res.body.modifyDate != null ? moment(res.body.modifyDate) : null;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((plan: IPlan) => {
        plan.targetDate = plan.targetDate != null ? moment(plan.targetDate) : null;
        plan.createDate = plan.createDate != null ? moment(plan.createDate) : null;
        plan.modifyDate = plan.modifyDate != null ? moment(plan.modifyDate) : null;
      });
    }
    return res;
  }
}
