import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs/internal/Observable';

@Injectable({
  providedIn: 'root',
})
export class Consultation {
   private http= inject(HttpClient)
   private apiUrl = 'http://localhost:8888/api/consultation';
   private apiUrls = 'http://localhost:8887/api/patient';

   ajouter(pid: string, consultationRequete: any): Observable<any>{
    return this.http.post<any>(`${this.apiUrl}/${pid}`, consultationRequete);
   }
    listerPatient(): Observable<any>{
    return this.http.get(`${this.apiUrls}`)
   }
   lister(): Observable<any>{
    return this.http.get(`${this.apiUrl}/liste`)
   }
   rechercher(mc: string): Observable<any> {
    return this.http.get(`${this.apiUrl}/recherche?mc=${encodeURIComponent(mc)}`);
  }
}
