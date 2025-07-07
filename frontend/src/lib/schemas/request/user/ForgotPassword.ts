import { z } from 'zod';

export const ForgotPasswordSchema = z.object({
	username: z.string().email('Invalid email address').nonempty('Email is required')
});

export type ForgotPassword = z.infer<typeof ForgotPasswordSchema>;
