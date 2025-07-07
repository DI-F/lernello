import type { Actions } from '@sveltejs/kit';
import { fail } from '@sveltejs/kit';
import { ApiError, handleApiError } from '$lib/api/apiError';
import { type Infer, message, setError, superValidate } from 'sveltekit-superforms';
import { zod } from 'sveltekit-superforms/adapters';
import { forgotPassword } from '$lib/api/collections/auth';
import { api } from '$lib/api/apiClient.js';
import { ForgotPasswordSchema } from '$lib/schemas/request/user/ForgotPassword';

export const load = async () => {
	const form = await superValidate<Infer<typeof ForgotPasswordSchema>>(zod(ForgotPasswordSchema));
	return { form };
};

export const actions = {
	default: handleApiError(async ({ request, fetch }) => {
		const form = await superValidate<Infer<typeof ForgotPasswordSchema>>(
			request,
			zod(ForgotPasswordSchema)
		);

		if (!form.valid) {
			return fail(400, { form });
		}

		try {
			await api(fetch).req(forgotPassword, form.data).response;

			return message(form, {
				// Kein tokenInfo/redirectTo notwendig
			});
		} catch (error) {
			setError(
				form,
				'username',
				'An error occurred while processing your request. Please try again later.'
			);
			if (error instanceof ApiError) {
				// Optional: Generische Fehlermeldung → keine Infos leaken
				setError(
					form,
					'username',
					'An error occurred while processing your request. Please try again later.'
				);
			}

			throw error;
		}
	})
} satisfies Actions;
