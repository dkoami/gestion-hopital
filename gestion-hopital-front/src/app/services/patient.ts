import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class Patient {
   private http= inject(HttpClient)
   private apiUrl = 'http://localhost:8887/api/patient';

   ajouter(patient:any): Observable<any>{
    return this.http.post(`${this.apiUrl}/ajout`,patient)
   }

   lister(): Observable<any>{
    return this.http.get(`${this.apiUrl}`)
   }

   obtenir(id: string): Observable<any> {
    return this.http.get(`${this.apiUrl}/${id}`);
  }
  modifier(id: string, patient: any): Observable<any> {
    return this.http.put(`${this.apiUrl}/${id}`, patient);
  }

  // Rechercher par mot-clé via l'API @GetMapping("/recherche")
  rechercher(mc: string): Observable<any> {
    return this.http.get(`${this.apiUrl}/recherche?mc=${encodeURIComponent(mc)}`);
  }
  
  supprimer(pid: string): Observable<string> {
    return this.http.delete(`${this.apiUrl}/${pid}`, { responseType: 'text' });
  }
}
