import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IExcerciseType } from 'app/shared/model/excercise-type.model';

type EntityResponseType = HttpResponse<IExcerciseType>;
type EntityArrayResponseType = HttpResponse<IExcerciseType[]>;

@Injectable({ providedIn: 'root' })
export class ExcerciseTypeService {
  public resourceUrl = SERVER_API_URL + 'api/excercise-types';

  constructor(protected http: HttpClient) {}

  create(excerciseType: IExcerciseType): Observable<EntityResponseType> {
    return this.http.post<IExcerciseType>(this.resourceUrl, excerciseType, { observe: 'response' });
  }

  update(excerciseType: IExcerciseType): Observable<EntityResponseType> {
    return this.http.put<IExcerciseType>(this.resourceUrl, excerciseType, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IExcerciseType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IExcerciseType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
