import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IExerciseConfig } from 'app/shared/model/exercise-config.model';

type EntityResponseType = HttpResponse<IExerciseConfig>;
type EntityArrayResponseType = HttpResponse<IExerciseConfig[]>;

@Injectable({ providedIn: 'root' })
export class ExerciseConfigService {
  public resourceUrl = SERVER_API_URL + 'api/exercise-configs';

  constructor(protected http: HttpClient) {}

  create(exerciseConfig: IExerciseConfig): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(exerciseConfig);
    return this.http
      .post<IExerciseConfig>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(exerciseConfig: IExerciseConfig): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(exerciseConfig);
    return this.http
      .put<IExerciseConfig>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IExerciseConfig>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IExerciseConfig[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(exerciseConfig: IExerciseConfig): IExerciseConfig {
    const copy: IExerciseConfig = Object.assign({}, exerciseConfig, {
      createDate: exerciseConfig.createDate != null && exerciseConfig.createDate.isValid() ? exerciseConfig.createDate.toJSON() : null,
      modifyDate: exerciseConfig.modifyDate != null && exerciseConfig.modifyDate.isValid() ? exerciseConfig.modifyDate.toJSON() : null
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
      res.body.forEach((exerciseConfig: IExerciseConfig) => {
        exerciseConfig.createDate = exerciseConfig.createDate != null ? moment(exerciseConfig.createDate) : null;
        exerciseConfig.modifyDate = exerciseConfig.modifyDate != null ? moment(exerciseConfig.modifyDate) : null;
      });
    }
    return res;
  }
}
