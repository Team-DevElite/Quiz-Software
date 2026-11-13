export interface Quiz {
  id: number;
  name: string;
  description: string;
  courseCode: string;
  course_code?: string;
  isPublished: boolean;
  createdAt?: string;
}

