import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs/internal/Observable';

@Injectable({
  providedIn: 'root',
})
export class Sejour {
  private http= inject(HttpClient)
  private apiUrl = 'http://localhost:8888/api/sejour';
  private apiUrls = 'http://localhost:8887/api/patient';

   ajouter(sejourRequete: any): Observable<any>{
    return this.http.post<any>(`${this.apiUrl}`, sejourRequete);
   }
    listerPatient(): Observable<any>{
    return this.http.get(`${this.apiUrls}`)
   }
   lister(): Observable<any>{
    return this.http.get(`${this.apiUrl}/liste`)
  }
  terminerSejour(sid: number): Observable<any> {
    return this.http.patch<any>(`${this.apiUrl}/${sid}/terminer`, {});
  }

 annulerSejour(sid: number): Observable<any> {
    return this.http.patch<any>(`${this.apiUrl}/${sid}/annuler`, {});
  }
}
