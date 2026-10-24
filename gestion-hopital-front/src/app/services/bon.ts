import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class Bon {
  private http= inject(HttpClient)
  private apiUrl = 'http://localhost:8887/api/bon';

  ajouter(bon:any): Observable<any>{
    return this.http.post(`${this.apiUrl}/ajout`,bon)
  }
   lister(): Observable<any>{
    return this.http.get(`${this.apiUrl}`)
   }
   modifier(id: string, bont: any): Observable<any> {
    return this.http.put(`${this.apiUrl}/${id}`, bont);
  }
     obtenir(id: string): Observable<any> {
    return this.http.get(`${this.apiUrl}/${id}`);
  }
}
