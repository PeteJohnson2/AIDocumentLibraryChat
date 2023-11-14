/**
 *    Copyright 2023 Sven Loesekann
   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at
       http://www.apache.org/licenses/LICENSE-2.0
   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
import { Component, DestroyRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import {MatToolbarModule} from '@angular/material/toolbar'; 
import {MatButtonModule} from '@angular/material/button';
import {MatTableModule} from '@angular/material/table';
import {MatInputModule} from '@angular/material/input';
import {MatFormFieldModule} from '@angular/material/form-field';
import {FormControl, FormsModule,ReactiveFormsModule, Validators} from '@angular/forms';
import { Router } from '@angular/router';
import { DocumentService } from '../service/document.service';
import { DocumentSearch } from '../model/documents';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { map, tap } from 'rxjs/operators';
import {MatProgressSpinnerModule} from '@angular/material/progress-spinner'; 
import { interval } from 'rxjs';


@Component({
  selector: 'app-doc-search',
  standalone: true,
  imports: [CommonModule,MatToolbarModule,MatButtonModule,MatTableModule,MatInputModule,MatFormFieldModule,FormsModule,ReactiveFormsModule,MatProgressSpinnerModule],
  templateUrl: './doc-search.component.html',
  styleUrls: ['./doc-search.component.scss']
})
export class DocSearchComponent {        
	protected searchValueControl = new FormControl('', [Validators.required, Validators.minLength(3)]);
	protected searchResults: string[] = [];
	protected searching = false;
	protected msWorking = 0;
	
    constructor(private destroyRef: DestroyRef, private router: Router, private documentService: DocumentService) { }
    
	protected showList(): void {
		this.router.navigate(['/doclist']);
	}
	
	protected search(): void {
		const startDate = new Date();
		this.msWorking = 0;
		this.searching = true;
		const repeatSub = interval(100).pipe(map(() => new Date())).subscribe(newDate => this.msWorking = newDate.getTime() - startDate.getTime());
		const documentSearch = {searchString: this.searchValueControl.value} as DocumentSearch;
		this.documentService.postDocumentSearch(documentSearch)
		  .pipe(takeUntilDestroyed(this.destroyRef), tap(() => this.searching = false), tap(() => repeatSub.unsubscribe()))
		  .subscribe(result => {
			  this.searchResults = result.resultStrings;
			  //console.log(this.searchResults);
			  });
	}
	
	protected logout(): void {
		console.log('logout');
	}
}
