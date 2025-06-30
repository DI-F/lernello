package ch.nova_omnia.lernello.block.service;

import ch.nova_omnia.lernello.aiClient.service.AIClientService;
import ch.nova_omnia.lernello.block.model.Block;
import ch.nova_omnia.lernello.block.model.BlockLanguage;
import ch.nova_omnia.lernello.block.model.TranslatedBlock;
import ch.nova_omnia.lernello.block.model.quiz.MultipleChoiceBlock;
import ch.nova_omnia.lernello.block.model.quiz.QuestionBlock;
import ch.nova_omnia.lernello.block.repository.BlockRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TranslationService {

    private final AIClientService aiClient;
    private final BlockRepository blockRepository;

    @Transactional
    public void translateAndSave(Block block, BlockLanguage lang, String content) {
        String prompt = AIPromptTemplate.TRANSLATION.format(lang.name(), content);
        String translated = aiClient.sendPrompt(prompt);

        TranslatedBlock translatedBlock = new TranslatedBlock();
        translatedBlock.setLanguage(lang);
        translatedBlock.setContent(translated);
        translatedBlock.setOriginalBlock(block);
        translatedBlock.setLearningUnit(block.getLearningUnit());
        translatedBlock.setPosition(block.getPosition());
        translatedBlock.setType(block.getType());

        translatedBlock.setName(
            aiClient.sendPrompt(AIPromptTemplate.TRANSLATION.format(lang.name(), block.getName()))
        );

        blockRepository.save(translatedBlock);
    }

    @Transactional
    public void saveQuestionTranslation(QuestionBlock block, BlockLanguage lang) {
        TranslatedBlock translatedBlock = new TranslatedBlock();
        translatedBlock.setLanguage(lang);
        translatedBlock.setQuestion(aiClient.sendPrompt(AIPromptTemplate.TRANSLATION.format(lang.name(), block.getQuestion())));
        translatedBlock.setExpectedAnswer(aiClient.sendPrompt(AIPromptTemplate.TRANSLATION.format(lang.name(), block.getExpectedAnswer())));
        translatedBlock.setOriginalBlock(block);
        translatedBlock.setLearningUnit(block.getLearningUnit());
        translatedBlock.setPosition(block.getPosition());
        translatedBlock.setType(block.getType());
        translatedBlock.setName(aiClient.sendPrompt(AIPromptTemplate.TRANSLATION.format(lang.name(), block.getName())));

        blockRepository.save(translatedBlock);
    }

    @Transactional
    public void saveMultipleChoiceTranslation(MultipleChoiceBlock block, BlockLanguage lang) {
        TranslatedBlock translatedBlock = new TranslatedBlock();
        translatedBlock.setLanguage(lang);
        translatedBlock.setQuestion(aiClient.sendPrompt(AIPromptTemplate.TRANSLATION.format(lang.name(), block.getQuestion())));
        translatedBlock.setPossibleAnswers(
            block.getPossibleAnswers().stream()
                .map(ans -> aiClient.sendPrompt(AIPromptTemplate.TRANSLATION.format(lang.name(), ans)))
                .toList()
        );
        translatedBlock.setCorrectAnswers(
            block.getCorrectAnswers().stream()
                .map(ans -> aiClient.sendPrompt(AIPromptTemplate.TRANSLATION.format(lang.name(), ans)))
                .toList()
        );
        translatedBlock.setOriginalBlock(block);
        translatedBlock.setLearningUnit(block.getLearningUnit());
        translatedBlock.setPosition(block.getPosition());
        translatedBlock.setType(block.getType());
        translatedBlock.setName(aiClient.sendPrompt(AIPromptTemplate.TRANSLATION.format(lang.name(), block.getName())));

        blockRepository.save(translatedBlock);
    }
}
