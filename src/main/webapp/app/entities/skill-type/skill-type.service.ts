import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ISkillType } from 'app/shared/model/skill-type.model';

type EntityResponseType = HttpResponse<ISkillType>;
type EntityArrayResponseType = HttpResponse<ISkillType[]>;

@Injectable({ providedIn: 'root' })
export class SkillTypeService {
  public resourceUrl = SERVER_API_URL + 'api/skill-types';

  constructor(protected http: HttpClient) {}

  create(skillType: ISkillType): Observable<EntityResponseType> {
    return this.http.post<ISkillType>(this.resourceUrl, skillType, { observe: 'response' });
  }

  update(skillType: ISkillType): Observable<EntityResponseType> {
    return this.http.put<ISkillType>(this.resourceUrl, skillType, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISkillType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISkillType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
