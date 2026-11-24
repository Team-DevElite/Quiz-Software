/** Category as returned inside quiz JSON (backend Category entity, quizzes list is @JsonIgnore'd). */
export interface Category {
  id: number;
  name: string;
  description?: string | null;
}

export interface Quiz {
  id: number;
  name: string;
  description: string;
  courseCode: string;
  course_code?: string;
  isPublished: boolean;
  createdAt?: string;
  /** null when the teacher has not assigned a category. */
  category?: Category | null;
}

