/**
 * Copyright 2012 plista GmbH  (http://www.plista.com/)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and limitations under the License.
 */

package org.plista.kornakapi.core.training;

import org.apache.mahout.cf.taste.impl.recommender.svd.ALSWRFactorizer;
import org.apache.mahout.cf.taste.impl.recommender.svd.Factorization;
import org.apache.mahout.cf.taste.impl.recommender.svd.FilePersistenceStrategy;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.plista.kornakapi.core.config.FactorizationbasedRecommenderConfig;
import org.plista.kornakapi.core.storage.Storage;

import java.io.File;
import java.io.IOException;

public class FactorizationbasedInMemoryTrainer implements Trainer {

  private final FactorizationbasedRecommenderConfig conf;

  public FactorizationbasedInMemoryTrainer(FactorizationbasedRecommenderConfig conf) {
    this.conf = conf;
  }

  @Override
  public void train(File modelDirectory, Storage storage, Recommender recommender) throws IOException {

    try {

      ALSWRFactorizer factorizer = new ALSWRFactorizer(storage.trainingData(), conf.getNumberOfFeatures(),
          conf.getLambda(), conf.getNumberOfIterations(), conf.isUsesImplicitFeedback(), conf.getAlpha());

      Factorization factorization = factorizer.factorize();

      new FilePersistenceStrategy(new File(modelDirectory, conf.getName() + ".model")).maybePersist(factorization);

      recommender.refresh(null);

    } catch (Exception e) {
      throw new IOException(e);
    }
  }
}
