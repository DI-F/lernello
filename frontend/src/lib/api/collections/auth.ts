import { UserLoginSchema } from '$lib/schemas/request/user/UserLogin';
import { LoggedInUserSchema } from '$lib/schemas/response/LoggedInUser';
import { createEndpoint } from '../createEndpoint';
import { ForgotPasswordSchema } from '$lib/schemas/request/user/ForgotPassword';
import { z } from 'zod';

const REQUEST_MAPPING = '/api/auth';

export const signin = createEndpoint({
	method: 'POST',
	getPath: () => `${REQUEST_MAPPING}/signin`,
	response: {
		schema: LoggedInUserSchema,
		defaultValidate: true
	},
	payload: {
		schema: UserLoginSchema,
		defaultValidate: false
	}
});

export const forgotPassword = createEndpoint({
	method: 'POST',
	getPath: () => `${REQUEST_MAPPING}/forgot-password`,
	response: {
		schema: z.null(), // No response expected for this endpoint to prevent user-leakage
		defaultValidate: false
	},
	payload: {
		schema: ForgotPasswordSchema,
		defaultValidate: false
	}
});
